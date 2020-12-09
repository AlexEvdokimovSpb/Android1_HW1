package gb.myhomework.android1.place;

import android.content.res.Resources;

public class CitySourceBuilder {
    private Resources resources;

    public CitySourceBuilder setResources(Resources resources){
        this.resources = resources;
        return this;
    }

    public CityDataSource build(){
        CitySource citySource = new CitySource(resources);
        citySource.init();
        return citySource;
    }
}
