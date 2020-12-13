package gb.myhomework.android1.place;

public class City {
    private String description;
    private int picture;

    public City(String description, int picture){
        this.description=description;
        this.picture=picture;
    }
    public String getDescription(){
        return description;
    }
    public int getPicture(){
        return picture;
    }
}
