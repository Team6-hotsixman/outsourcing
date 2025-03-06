package com.example.outsourcing.domain.common.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String filename ="";
    String imagePath="";

    @Builder
    public Image(long id,String filename, String imagePath) {
        this.id = id;
        this.filename = filename;
        this.imagePath = imagePath;
    }
}
