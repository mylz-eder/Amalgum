package model.entity.person;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder



public class Person {
    private int id, age;
    private String firstName, lastName ,experience;
    private Occupation occupation;
    private LocalDate birthDate;
    private boolean entryExp, middleExp, expertExp;

    public String toString () {return new Gson().toJson(this);
    }


}
