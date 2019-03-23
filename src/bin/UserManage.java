package bin;
import data.User;
import data.UserList;

public class UserManage {

    public static boolean registration(int qmNumber, String fullName, String email){
        if(!ifDuplicate(qmNumber, email)){
            UserList userList = new UserList();
            userList.addUser(new User(qmNumber, fullName, email));
            return true;
        }
        return false;
    }

    private static boolean ifDuplicate(int qmNumber, String email) {
        UserList userList = new UserList();
        for (User user:userList.getList()) {
            if (qmNumber == user.getQmNumber())
                return true;
            if (email.equals(user.getEmail()))
                return true;
        }
        return false;
    }
}

//
//