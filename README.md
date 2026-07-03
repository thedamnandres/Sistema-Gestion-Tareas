# Calidad de Software – Proyecto Base

Trabajo práctico de la asignatura **ISWZ3208 - Calidad de Software**.

## Descripción

Este repositorio parte de un **código base con problemas de calidad intencionales**.
El objetivo del trabajo es aplicar técnicas de calidad (Clean Code, principios SOLID,
análisis estático y CI/CD) para refactorizar y mejorar el código, midiendo el impacto
con métricas antes y después.

El proyecto base elegido es el **Sistema de Inventario** (`proyecto3.Inventory`).

## Problemas de calidad identificados (estado inicial)

- **Listas paralelas:** `products`, `quantities` y `prices` se manejan por índice
  en lugar de una clase `Product`.
- **Nombres poco descriptivos:** parámetros como `p`, `q`, `price`.
- **Baja cohesión:** métodos que mezclan lógica de negocio con impresión en consola.
- **Ausencia de manejo de errores:** no se validan duplicados, cantidades negativas
  ni entradas nulas.
- **Cobertura de pruebas nula:** no existen pruebas unitarias.
- **Tipos crudos (raw types):** uso de `List` sin genéricos.

## Herramientas de calidad

- **Checkstyle** – estilo de código
- **PMD** – detección de defectos y malas prácticas
- **SpotBugs** – análisis de bytecode
- **JaCoCo** – cobertura de pruebas
- **GitHub Actions** – integración continua (CI/CD)

## Cómo generar los reportes

mvn test                   # ejecuta pruebas y cobertura (JaCoCo)
mvn checkstyle:checkstyle  # reporte de estilo
mvn pmd:pmd                # reporte de defectos
mvn spotbugs:spotbugs      # análisis de bytecode
mvn site                   # reporte HTML integrado

## Equipo

## Equipo

| Integrante     | Roles |
|----------------|-------|
| Andres Jimenez | Líder del equipo |
| Galo Guevara   | Responsable de análisis estático (Checkstyle / PMD / SpotBugs) |
| Paul Larrea    | Responsable de métricas y cobertura (JaCoCo) |
| Pablo Criollo  | Responsable de revisión manual (Clean Code / SOLID) |
| Galo Guevara   | Responsable de CI/CD (GitHub Actions) y documentación |