# Java 14 new feature
## New switch case notation
In this version of java it is now possible to use  arrows to indicate the code to run in each case. 
And you can also assign values to variables or return statements with a switch case.For example: \

```
boolean isTodayHoliday = switch (day) {
    case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> false;
    case "SATURDAY", "SUNDAY" -> true;
    default -> throw new IllegalArgumentException("What's a " + day);
};
```





