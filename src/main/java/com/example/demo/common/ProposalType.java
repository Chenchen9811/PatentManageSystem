package com.example.demo.common;

public enum ProposalType {
    INVENT_PATENT("发明专利", 0),
    UTILITY_MODEL_PATENT("实用新型专利",1),
    EXTERIOR_DESIGN_PATENT("外观发明专利",2),
    SOFTWARE_COPYRIGHT("软著",3),
    TRADEMARK("商标",4);

    private String name;
    private Integer code;

    private ProposalType(String name, Integer code){
        this.name = name;
        this.code = code;
    }

    private String getName() {
        return name;
    }

    private Integer getCode() {
        return code;
    }
}
