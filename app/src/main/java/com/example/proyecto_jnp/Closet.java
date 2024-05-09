package com.example.proyecto_jnp;

/*import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;*/

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)

public class Closet extends Container{

}
