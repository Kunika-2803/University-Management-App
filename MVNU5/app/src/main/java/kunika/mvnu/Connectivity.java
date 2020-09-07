package kunika.mvnu;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;

public class Connectivity {
    private static final String url = "jdbc:mysql://192.168.43.30:3306/mvn_admin";
    private static final String user = "kk212";
    private static final String pass = "!Kunika_123";
    private static Statement st;
    private static ResultSet rs = null;
    private static PreparedStatement preparedStatement=null;
    public static void Connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("Databaseection success");

            String result = "Database Connection Successful\n";
            st = con.createStatement();

        }
        catch(Exception e)
        {

        }
    }
    public static ResultSet Login(String user,String pass)
    {

        String res = "";
        try {
            Connect();
            rs = st.executeQuery("select * from student_register where Roll_No='"+user+"' And Password='"+pass+"'");
            ResultSetMetaData rsmd = rs.getMetaData();
        } catch (Exception e) {
            e.printStackTrace();
            res = e.toString();
        }
        return rs;
    }
    public static ResultSet getLogin(String user)
    {

        String res = "";
        try {
            Connect();
            rs = st.executeQuery("select * from student_register Inner Join department on department.dept_id=student_register.dept_id where Roll_No='"+user+"'");
            ResultSetMetaData rsmd = rs.getMetaData();
        } catch (Exception e) {
            e.printStackTrace();
            res = e.toString();
        }
        return rs;
    }
    public static String UpdateDetails(String start_session,String end_session,String gender,String roll)
    {
        String res="";
        try
        {
            Connect();
            st.executeUpdate("Update student_register set start_session='"+start_session+"',end_session='"+end_session+"',gender='"+gender+"' where Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            res=e.toString();
        }
        return res;
    }
    public static String UpdatePassword(String roll,String pass1,String pass2)
    {
        String res="";
        try
        {
            Connect();
            st.executeUpdate("Update student_register set Password='"+pass2+"' where Roll_No='"+roll+"' And Password='"+pass1+"'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            res=e.toString();
        }
        return res;
    }
    public static String SetNewPassword(String roll,String pass2)
    {
        String res="";
        try
        {
            Connect();
            st.executeUpdate("Update student_register set Password='"+pass2+"' where Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            res=e.toString();
        }
        return res;
    }
    public static ResultSet Verify(String roll,String email)
    {
        try
        {
            Connect();
            rs=st.executeQuery("select * from student_register where Roll_No='"+roll+"' And email='"+email+"'");
        }
        catch (Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getTeacher(String user)
    {

        String res = "";
        try {
            Connect();
            rs = st.executeQuery("select fac_id from faculty where name='"+user+"'");
            ResultSetMetaData rsmd = rs.getMetaData();
        } catch (Exception e) {
            e.printStackTrace();
            res = e.toString();
        }
        return rs;
    }
    public static String Register(String name,String roll,String email,String gender,String mobile,String pass,String Course,String Sem)
    {
        String res="";
        try
        {
            Connect();
            st.executeUpdate("Insert into student_register(name,Roll_No,Email,Mobile_No,Password,dept_id,Sem,Gender) values('"+name+"','"+roll+"','"+email+"','"+mobile+"','"+pass+"','"+Course+"','"+Sem+"','"+gender+"')");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            res=e.toString();
        }
        return res;
    }
    public static ResultSet dept_id(String dept_name)
    {
        String res="";
        try
        {
            Connect();
            rs=st.executeQuery("select dept_id from department where dept_name='"+dept_name+"'");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            res=e.toString();
        }
        return rs;
    }
    public static ResultSet getCourse()
    {
        try
        {
            Connect();
            rs=st.executeQuery("select course_name from course");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  rs;
    }

    public static String OrganiseEvent(String roll,String course,String sem,String event_type,String event_name,String event_describe,String icon,String ref_teacher)
    {
        String res="";
        try
        {
            Connect();
            st.executeUpdate("insert into event_detail(dept_id,Event_Description,Event_icon,Event_name,Event_Type,Fac_ID,Roll_No,Sem) values('"+course+"','"+event_describe+"','"+icon+"','"+event_name+"','"+event_type+"','"+ref_teacher+"','"+roll+"','"+sem+"')");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            res=e.toString();
        }
        return res;
    }
    public static ResultSet getAttendance(String roll)
    {
        try
        {
            Connect();
            rs=st.executeQuery("SELECT attendance.present, course.course_name from attendance Inner Join course on course.course_id=attendance.course_id where Roll_No='"+roll+"'");
        }
        catch(Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getBooks(String roll)
    {
        try
        {
            Connect();
            rs=st.executeQuery("select library_books.book_id,library_books.book_title,library_book_issued.issue_date,library_book_issued.Return_date from library_books,library_book_issued where library_books.book_id<>library_book_issued.book_id And library_book_issued.Library_card_No=(select Library_card_No from library_borrower where Roll_No='"+roll+"')");
        }
        catch(Exception e)
        {

        }
        return rs;
    }
    public static  ResultSet getAllBooks()
    {
        try
        {
            Connect();
            rs=st.executeQuery("select book_title,book_author,no_of_Copies from library_books");
        }
        catch(Exception e)
        {

        }
        return rs;
    }
    public static ResultSet Reissue(String book_id,Date issue_date,Date return_date)
    {
        try
        {
            Connect();
            st.executeUpdate("Update library_book_issued set issue_date='"+issue_date+"' and Return_date='"+return_date+"' where book_id='"+book_id+"'");
        }
        catch(Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getTimeTable(String day_name)
    {
        try
        {
            Connect();
            rs=st.executeQuery("select course.course_name,faculty.name,tt_reference_periods.period_no,tt_reference_periods.start_time,tt_reference_periods.end_time from tt_reference_periods Inner Join tt_planned_tt on tt_reference_periods.period_no=tt_planned_tt.period_no Inner Join course on course.course_id=tt_planned_tt.course_id INNER JOIN tt_generated_tt on tt_generated_tt.course_id=tt_planned_tt.course_id INNER JOIN faculty on faculty.fac_id=tt_generated_tt.fac_id where tt_planned_tt.day_name='"+day_name+"' order by tt_reference_periods.period_no");
        }
        catch (Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getCourseName(String roll)
    {
        try
        {
            Connect();
            rs=st.executeQuery("select course.course_name from course INNER Join student_takes On course.course_id=student_takes.course_id where Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getDepartment(String roll)
    {
        try
        {
            Connect();
            rs=st.executeQuery("select department.dept_name,student_register.Sem from department Inner Join student_register on department.dept_id=student_register.dept_id where Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getDateSheet(String roll)
    {
        try
        {
            Connect();
            rs=st.executeQuery("select exam_calendar.date,exam_schedule_time.start_time,exam_schedule_time.end_time,course.course_name,exam_schedule_time.Session from exam_schedule Inner Join exam_calendar On exam_schedule.cal_id=exam_calendar.id Inner Join exam_schedule_time on exam_schedule.schedule_id=exam_schedule_time.schedule_id Inner Join course on course.course_id=exam_schedule.course_id where exam_schedule.dept_id=(select dept_id from student_register where Roll_No='"+roll+"') And exam_schedule.sem=(SELECT Sem from student_register where Roll_No='"+roll+"') order by exam_calendar.date");
        }
        catch(Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getGradeSheet(String roll)
    {
        try
        {
            Connect();
            rs=st.executeQuery("select course.course_name,exam_grade_sheet.grades from course Inner Join student_takes on course.course_id=student_takes.course_id Inner Join exam_grade_sheet on exam_grade_sheet.take_id=student_takes.take_id where student_takes.Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {

        }
        return rs;
    }
    public static ResultSet getImage(String roll)
    {
        String blob = null;
        try
        {
            Connect();
            rs=st.executeQuery("select Image from student_register");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            blob=e.toString();
        }
        return rs;
    }
    public static String UploadImage(InputStream imageStream,int imageFileLength,String roll)
    {
        String res="";
        try {
            Connect();
            st.executeUpdate("Update student_register set Image='"+imageStream+"' where Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            res=e.toString();
        }
        return res;
    }
    public static ResultSet getAttendancePercent(String roll)
    {
        String blob = null;
        try
        {
            Connect();
            rs=st.executeQuery("select AVG(present) from attendance where Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            blob=e.toString();
        }
        return rs;
    }
    public static ResultSet getSyllabus(String roll,int Unit)
    {
        String blob = null;
        try
        {
            Connect();
            rs=st.executeQuery("SELECT topic.topic_name from topic Inner Join student_takes on topic.course_id=student_takes.course_id where topic.Unit="+Unit+" And student_takes.Roll_No='"+roll+"'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            blob=e.toString();
        }
        return rs;
    }
}

