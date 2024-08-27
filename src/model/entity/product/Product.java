package model.entity.product;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder


public class Product {
    private int id, count, price;
    private String name;
    private Status status;
    private Category category;

    public String toString () {return new Gson().toJson(this);
    }
}
