
package coursewebsite.beans;

import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import coursewebsite.client.PersistenceClient;
import java.io.IOException;


@Named(value = "loginBean")

@SessionScoped
public class LoginBean implements Serializable {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;
    
    private String username = "";
    private String password = "";
    private static User currentUser;

    public String studentLogsIn() throws IOException {
        try {
            User user = PersistenceClient.getInstance().checkPassword(username, password);
            if (user != null && "student".equals(user.getCategory())) {
                currentUser = user;
                return "/StudentPage/StudentMainPage.xhtml?faces-redirect=true"; 
            }
        } catch (DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        return "/MainPage/MainPage.xhtml?faces-redirect=true";
    }
    
    public String teacherLogsIn() {
        try {
            User user = PersistenceClient.getInstance().checkPassword(username, password);
            if (user != null && "teacher".equals(user.getCategory())) {
                currentUser = user;
                return "/TeacherPage/TeacherMainPage.xhtml?faces-redirect=true";
            }
        } catch (DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        return "/MainPage/MainPage.xhtml?faces-redirect=true";
    }
    
    /*protected User findUserByUsername() throws DoesNotExistException {
        Query query = em.createNamedQuery("User.findByUsername", User.class);
        List<User> s = query.setParameter("username", username).getResultList();
        if (s.size() > 0) {
            return s.get(0);
        }
        throw new DoesNotExistException("The user " + username + " does not exist.");
    }*/
    
    public String userLogsout() {
        currentUser = null;
        return "/MainPage/MainPage.xhtml?faces-redirect=true";
    }

    public static User getUserLoggedIn(){return currentUser;}

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setCurrentUser(User  currentUser) {
        this.currentUser = currentUser;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public User getCurrentUser() {
        return currentUser;
    }
}
