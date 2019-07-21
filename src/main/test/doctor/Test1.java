package doctor;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
    @Test
    public void test01() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(date);
        System.out.println(endTime);

        System.out.println(System.currentTimeMillis());
        System.out.println(Calendar.getInstance().getTimeInMillis());
        System.out.println(String.valueOf(System.currentTimeMillis()).substring(0, 10));
        System.out.println(new Timestamp(System.currentTimeMillis()));

        String str = "*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？";
        System.out.println(str);
        System.out.println(StringFilter(str));
    }

    @Test
    public void test02() {
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void test03() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String[] dateDivide = "2019-04-03".toString().split("-");

            int year = Integer.parseInt(dateDivide[0].trim());//去掉空格

            int month = Integer.parseInt(dateDivide[1].trim());

            int day = Integer.parseInt(dateDivide[2].trim());

            Calendar c = Calendar.getInstance();//获取一个日历实例

            c.set(year, month - 1, day);//设定日历的日期

            c.add(Calendar.MONDAY, 18);

            System.out.println(sdf.format(c.getTime()));

            String data = "2019-04-03";
            System.out.println(data.replaceAll("/", "-"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String StringFilter(String str) {
// 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
// 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
