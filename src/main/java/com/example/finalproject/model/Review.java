package com.example.finalproject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewCode;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private int rating;

    @Column()
    private String comment;

    @ManyToOne
    @JoinColumn(name = "advertisement_code")
    private Advertisement advertisement;

    @ManyToOne
    @JoinColumn(name = "buyer_code")
    private Buyer buyer;
}
