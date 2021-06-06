package tusba.rhytz.models;

import java.util.Hashtable;
import java.util.List;

public interface FirebaseInterface {

    public void AddAudioToFirebaseResult(boolean result);
    public void AddMusicianToFirebaseResult(boolean result);
    public void AddUserToFirebaseResult(boolean result);
    public void GetCategoriesResult(Hashtable<String, String> list);
    public void GetAllMusicResult(List<Music> list);
    public void GetMusicWithGenreResult(List<Music> list);
    public void GetMusicWithMusicianIdResult(List<Music> list);
    public void GetAllMusicianResult(List<Musician> list);
    public void GetMusicianWithIdResult(List<Musician> list);
    public void GetUserInfoWithMailResult(User user);
    public void CheckMailExistResult(boolean result);
    public void CheckUsernameExistResult(boolean result);
    public void LoginToAppResult(User user);
    public void UpdateUser(boolean result);
    public void GetUserResult(User user);
    public void TESTINT(List<String> list);
}

