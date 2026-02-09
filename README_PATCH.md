
# ITS255 v3.2 — Full-Record Display & SCCF-Optional Patch

This patch addresses the issue where the screen shows **only SCCF** after the SCCF search addon.
It restores **type-first + copybook-driven rendering** for every record and makes SCCF **optional**
so types like **9D** display correctly even when SCCF is not present.

## What’s included

- `SchemaHtmlRenderer.java` — renders a single record as an HTML table using the active schema
  from `com.its255.schema.Schemas`. Supports ALPHA/NUMERIC_TEXT, PACKED_DECIMAL (COMP-3) and
  BINARY via `com.its255.io.Fixed255Parser` (reflection), just like your CSV exporter.

- `FastRecordFilter_PATCHED.java` — same API as your current filter, but trims the Type values on
  comparison (so user input `1` matches stored `"1 "`), which avoids missed hits.

- `FastViewerController_PATCHED.java` — a drop-in controller variant whose `GET /viewer3/current`
  uses `SchemaHtmlRenderer` to build the `model.html` payload.

## How to apply

1. Copy `SchemaHtmlRenderer.java` into your project under `src/main/java/com/its255/viewer/`.
2. Either:
   - Replace your current `FastViewerController` class with the provided `FastViewerController_PATCHED.java`
     (rename it to `FastViewerController.java`), **or**
   - Manually replace only the body of your existing `current(...)` method with the one in the patch.
3. Replace your existing `FastRecordFilter` with `FastRecordFilter_PATCHED.java` (or trim on compare).
4. Rebuild and hit `/viewer3/current` — you should now see a full field table below the SCCF/Type header.

## Notes
- No changes are required to your `viewer3.html` — it already binds `model.html`.
- This patch uses your existing `Schemas`, `FieldSpec`, and `RecordType` to obtain per-type layouts.
- Decoding logic defers to your `com.its255.io.Fixed255Parser` so financial fields and binary decimals
  display correctly and match your CSV export.
