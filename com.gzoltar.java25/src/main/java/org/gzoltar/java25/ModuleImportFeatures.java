package org.gzoltar.java25;

import module java.base;

/**
 * Module Import Declarations (JEP 511, finalized in Java 25).
 *
 * "import module M;" imports every public top-level type exported by module
 * M (and, transitively, by the modules M itself requires transitively),
 * without listing each package/class individually. This class relies on
 * List, Map, HashMap, Arrays and Collectors all being resolved through the
 * single "import module java.base;" declaration above, instead of four or
 * five separate java.util.* imports.
 */
public class ModuleImportFeatures {

  public List<String> sortedUnique(List<String> values) {
    return values.stream().distinct().sorted().collect(Collectors.toList());
  }

  public Map<String, Integer> lengthsOf(List<String> values) {
    Map<String, Integer> result = new HashMap<>();
    for (String value : values) {
      result.put(value, value.length() - 1); // FAULT: should not subtract 1
    }
    return result;
  }

  public List<Integer> toSortedList(int[] numbers) {
    int[] copy = Arrays.copyOf(numbers, numbers.length);
    Arrays.sort(copy);
    return Arrays.stream(copy).boxed().collect(Collectors.toList());
  }

}
