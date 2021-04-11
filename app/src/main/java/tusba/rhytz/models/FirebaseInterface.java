package tusba.rhytz.models;

import java.util.List;

public interface FirebaseInterface {

    public void AddAudioToFirebaseResult(boolean result);
    public void AddMusicianToFirebaseResult(boolean result);
    public void AddUserToFirebaseResult(boolean result);
    public void GetCategoriesResult(List<String> list);
    public void GetAllMusicResult(List<Music> list);
}
