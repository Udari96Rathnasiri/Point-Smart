package lk.ijse.dep11.pos.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {
    private String code;
    private String description;
    private int qty;
    private BigDecimal unitPrice;
    private transient JFXButton delete;
}