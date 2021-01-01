import java.time.*;

/**
 * Calendar
 * version 1
 */

public class CalendarTest
{
    public static void main(String[] args)
    {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int tody = date.getDayOfMonth();

        date = date.minusDays(tody - 1);
        DayOfWeek weekday = date.getDayOfWeek();
        int value = weekday.getValue();

        System.out.println("Mon Tue Wed Thu Fri Sat Sun");
        for (int i = 1; i < value; i++)
            System.out.print("    ");
        while (date.getMonthValue() == month )
        {
            System.out.printf("%3d", date.getDayOfMonth());
            if (date.getDayOfMonth() == tody)
                System.out.print("*");
            else
                System.out.print(" ");
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 1) System.out.println();

        }
        if (date.getDayOfWeek().getValue() != 1) System.out.println();
    }
}
