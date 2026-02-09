
package com.its255.viewer;

import java.util.Optional;

public record RecordQuery(
        Optional<String> sccf,
        Optional<String> recordType,
        Optional<Integer> recordNumber
) {}
