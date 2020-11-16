import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


class MyPeriod{
    LocalTime startTime;
    LocalTime endTime;

    MyPeriod(LocalTime startTime,LocalTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "MyPeriod{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

class SortByStartTime implements Comparator<MyPeriod> {
    @Override
    public int compare(MyPeriod o1, MyPeriod o2) {
        if (o1.equals(o2)){
            return 0;
        }
        return o1.startTime.isBefore(o2.startTime)?-1:1;
    }
}
public class TestOverlap {
    public static void main(String[] args) throws FileNotFoundException {
        LinkedList<Scanner> scanners = new LinkedList<>();
        for (int i=0;i<3;i++){
            File f = new File("mylog"+i+".txt");
            Scanner sc = new Scanner(f);
            scanners.add(sc);
        }


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

        boolean end = false;
        LocalTime prev = null;

        List<MyPeriod> all_periods = new ArrayList<>();
        while (!end) {
            for (int i = 0; i < scanners.size(); i++) {
                Scanner cur_sc = scanners.get(i);
                if (cur_sc.hasNext()){
                    String line = cur_sc.nextLine();
                    String[] arr = line.split("#");
//                    System.out.println(arr[1].trim());

                    String startTime_str = arr[2].trim().split(" ")[1];
                    String endTime_str = arr[3].trim().split(" ")[1];
                    LocalTime curStart = LocalTime.parse(startTime_str, dateTimeFormatter);

                    LocalTime curEnd = LocalTime.parse(endTime_str, dateTimeFormatter);

                    all_periods.add(new MyPeriod(curStart,curEnd));
                }
                else {
                    end = true;
                }
            }
        }
        System.out.println(all_periods.size());
        if (checkOverlap(all_periods)){
            System.out.println("Overlap");
        }else{
            System.out.println("No Overlap");
        }

    }

    private static boolean checkOverlap(List<MyPeriod> all_periods) {
        Collections.sort(all_periods,new SortByStartTime());
        for (MyPeriod p:all_periods){
            System.out.println(p);
        }

        for (int i=0;i<all_periods.size();i++){
            for(int j= i+1;j<all_periods.size();j++){
                if (all_periods.get(i).endTime.isAfter(all_periods.get(j).startTime)&&
                        all_periods.get(j).endTime.isAfter(all_periods.get(i).startTime)){
                    System.out.println(i+" "+j);
                    System.out.println(all_periods.get(i));
                    System.out.println(all_periods.get(j));
                    return true;
                }

            }
        }
        return false;
    }
}
