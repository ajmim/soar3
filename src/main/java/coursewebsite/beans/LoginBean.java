
package coursewebsite.beans;

import static coursewebsite.beans.UserBean.findTeacherByUsername;
import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.Course;
import coursewebsite.models.Student;
import coursewebsite.models.Teacher;
import coursewebsite.models.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author IsmaTew
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @PersistenceContext(unitName = "soar_PU")
    private EntityManager em;
    
    private String username = "";
    private String password = "";
    private static Student currentStudent;
    private static Teacher currentTeacher;

    public String studentLogsIn() {
        try {
            Student student = findStudentByUsername(username);
            if (student != null && student.isPasswordCorrect(password)) {
                currentStudent = student;
                return "/StudentPage/StudentMainPage.xhtml?faces-redirect=true"; 
            }
        } catch (DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        return "/MainPage/MainPage.xhtml?faces-redirect=true";
    }
    
    protected Student findStudentByUsername(String username) throws DoesNotExistException {
        for (Student student : Database.getInstance().getStudents()) {
            if (student.getUsername().equals(username)) {
                return student;
            }
        }
        throw new DoesNotExistException("The student " + username + " does not exist.");
        
        Query query = em.createNamedQuery("Users.findByUsernameByUsername", User.class);
        List<Student> s = query.setParameter("username", username).getResultList();
        if (s.size() > 0) {
            return s.get(0);
        }
        throw new DoesNotExistException("The user " + username + " does not exist.");
    }
    }
    
    protected static Teacher findTeacherByUsername(String username) throws DoesNotExistException {
        for (Teacher teacher : Database.getInstance().getTeachers()) {
            if (teacher.getUsername().equals(username)) {
                return teacher;
            }
        }
        throw new DoesNotExistException("The teacher " + username + " does not exist.");
    }
    
    public String teacherLogsIn() {
        try {
            Teacher teacher = findTeacherByUsername(username);
            if (teacher != null && teacher.isPasswordCorrect(password)) {
                currentTeacher = teacher;
                return "/TeacherPage/TeacherMainPage.xhtml?faces-redirect=true";
            }
        } catch (DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        return "/MainPage/MainPage.xhtml?faces-redirect=true";
    }

  
    public String userLogsout() {
        currentStudent = null;
        currentTeacher = null;
        return "/MainPage/MainPage.xhtml?faces-redirect=true";
    }

    public static Student getStudentLoggedIn(){return currentStudent;}

    public static Teacher getTeacherLoggedIn() {return currentTeacher;}

    
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setCurrentStudent(Student  currentStudent) {
        this.currentStudent = currentStudent;
    }
    public void setCurrentTeacher(Teacher teacher){
        this.currentTeacher = currentTeacher;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Student getCurrentStudent() {
        return currentStudent;
    }
    public Teacher getCurrentTeacher() {
        return currentTeacher;
    }

    public Course doesCourseExistInUserCourses(Course course) throws AlreadyExistsException {
        for (Course c : LoginBean.getStudentLoggedIn().getUserCourses()) {
            if (course.equals(c)) {
                throw new AlreadyExistsException("This course is already in your list of courses.");
            }
        }
        return course;
    }
}
