#!/usr/bin/env python3
"""
generate_dashboard.py
Lee los reportes XML/CSV generados por Maven y los embebe directamente
dentro del index.html como variables JavaScript, produciendo un archivo
dashboard.html completamente autónomo (no necesita servidor web).
"""

import sys
import os
import json
import re
from xml.etree import ElementTree as ET
from datetime import datetime

# ── Rutas de entrada ───────────────────────────────────────────
# El job 'dashboard' ensambla todos los reportes en reports/ antes
# de ejecutar este script, así que leemos directamente desde ahí.
REPORTS_DIR     = "reports"
CHECKSTYLE_XML  = "reports/checkstyle-result.xml"
PMD_XML         = "reports/pmd.xml"
SPOTBUGS_XML    = "reports/spotbugs.xml"
JACOCO_CSV      = "reports/jacoco.csv"
TEMPLATE_HTML   = "reports/index.html"
OUTPUT_HTML     = "reports/dashboard.html"

# ── Helpers ────────────────────────────────────────────────────
def short_name(path):
    if not path:
        return "–"
    return path.replace("\\", "/").split("/")[-1]

def safe_int(val, default=0):
    try:
        return int(val)
    except (TypeError, ValueError):
        return default

def pct(covered, missed):
    total = covered + missed
    if total == 0:
        return 100
    return round(covered / total * 100)

# ── Parsers ────────────────────────────────────────────────────
def parse_checkstyle(path):
    if not os.path.exists(path):
        return []
    tree = ET.parse(path)
    root = tree.getroot()
    rows = []
    for file_el in root.findall("file"):
        fname = short_name(file_el.get("name", ""))
        for err in file_el.findall("error"):
            source = err.get("source", "")
            rule   = source.split(".")[-1] if source else "–"
            rows.append({
                "file":    fname,
                "line":    err.get("line",   "–"),
                "col":     err.get("column", "–"),
                "rule":    rule,
                "message": err.get("message", "–"),
            })
    return rows

def parse_pmd(path):
    if not os.path.exists(path):
        return []
    tree = ET.parse(path)
    root = tree.getroot()
    # namespace aware
    ns = {"pmd": "http://pmd.sourceforge.net/report/2.0.0"}
    rows = []
    for file_el in root.findall(".//file") + root.findall(".//pmd:file", ns):
        fname = short_name(file_el.get("name", ""))
        for v in file_el:
            if v.tag.endswith("violation") or v.tag == "violation":
                rows.append({
                    "file":     fname,
                    "line":     v.get("beginline", "–"),
                    "priority": v.get("priority",  "5"),
                    "rule":     v.get("rule",       "–"),
                    "message":  (v.text or "–").strip(),
                })
    return rows

def parse_spotbugs(path):
    if not os.path.exists(path):
        return []
    tree = ET.parse(path)
    root = tree.getroot()
    bugs = []
    for bug in root.findall(".//BugInstance"):
        cls_el    = bug.find("Class")
        method_el = bug.find("Method")
        src_el    = bug.find("SourceLine")
        clsname   = cls_el.get("classname", "–") if cls_el is not None else "–"
        method    = method_el.get("name", "–")    if method_el is not None else "–"
        line      = src_el.get("start", "–")      if src_el is not None else "–"
        bugs.append({
            "type":     bug.get("type",     "–"),
            "priority": bug.get("priority", "–"),
            "category": bug.get("category", "–"),
            "cls":      clsname.split(".")[-1],
            "method":   method,
            "line":     line,
        })
    return bugs

def parse_jacoco_csv(path):
    if not os.path.exists(path):
        return []
    rows = []
    with open(path, encoding="utf-8") as f:
        lines = [l.strip() for l in f if l.strip()]
    for line in lines[1:]:   # skip header
        cols = line.split(",")
        if len(cols) < 13:
            continue
        rows.append({
            "group":    cols[0],
            "pkg":      cols[1],
            "cls":      cols[2],
            "iMissed":  safe_int(cols[3]),
            "iCov":     safe_int(cols[4]),
            "bMissed":  safe_int(cols[5]),
            "bCov":     safe_int(cols[6]),
            "lMissed":  safe_int(cols[7]),
            "lCov":     safe_int(cols[8]),
            "cxMissed": safe_int(cols[9]),
            "cxCov":    safe_int(cols[10]),
            "mMissed":  safe_int(cols[11]),
            "mCov":     safe_int(cols[12]),
        })
    return rows

# ── Main ───────────────────────────────────────────────────────
def main():
    print("=== generate_dashboard.py ===")

    cs_rows = parse_checkstyle(CHECKSTYLE_XML)
    pmd_rows = parse_pmd(PMD_XML)
    sb_bugs  = parse_spotbugs(SPOTBUGS_XML)
    jc_rows  = parse_jacoco_csv(JACOCO_CSV)

    print(f"  Checkstyle : {len(cs_rows)} violaciones")
    print(f"  PMD        : {len(pmd_rows)} violaciones")
    print(f"  SpotBugs   : {len(sb_bugs)} bugs")
    print(f"  JaCoCo     : {len(jc_rows)} clases")

    # Totales JaCoCo
    totals = {k: 0 for k in ["iMissed","iCov","bMissed","bCov","lMissed","lCov","mMissed","mCov"]}
    for r in jc_rows:
        for k in totals:
            totals[k] += r[k]

    report_data = {
        "generatedAt": datetime.utcnow().strftime("%Y-%m-%dT%H:%M:%SZ"),
        "checkstyle":  cs_rows,
        "pmd":         pmd_rows,
        "spotbugs":    sb_bugs,
        "jacoco":      jc_rows,
        "jacocoTotals": totals,
    }

    # Leer template
    if not os.path.exists(TEMPLATE_HTML):
        print(f"ERROR: no se encontró {TEMPLATE_HTML}", file=sys.stderr)
        sys.exit(1)

    with open(TEMPLATE_HTML, encoding="utf-8") as f:
        html = f.read()

    # Inyectar bloque de datos antes del </head>
    data_script = (
        "\n<script>\n"
        "// Datos embebidos por generate_dashboard.py durante el CI\n"
        f"window.REPORT_DATA = {json.dumps(report_data, ensure_ascii=False, indent=2)};\n"
        "</script>\n"
    )
    html = html.replace("</head>", data_script + "</head>", 1)

    # Escribir dashboard autónomo
    os.makedirs(REPORTS_DIR, exist_ok=True)
    with open(OUTPUT_HTML, "w", encoding="utf-8") as f:
        f.write(html)

    print(f"  => Dashboard autónomo escrito en {OUTPUT_HTML}")

if __name__ == "__main__":
    main()
