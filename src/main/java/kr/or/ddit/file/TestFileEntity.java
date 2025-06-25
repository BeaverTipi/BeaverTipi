package kr.or.ddit.file;

import lombok.Data;

//@Entity
@Data
public class TestFileEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imgText;

    private String imgUrl;

}