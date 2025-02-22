package myshop.shop.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Product  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)

    String name;
    @Lob
    @Column(columnDefinition = "TEXT")
    String description;
    double price;
    @Lob
    Blob image;
    int stock;

    @ManyToOne
    private Category category;

    @ManyToOne
    private ProductBrand brand;
    public String getImageAsBase64() {
        if (image != null) {
            try {
                byte[] imageBytes = image.getBytes(1, (int) image.length());
                return Base64.getEncoder().encodeToString(imageBytes);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @JsonProperty("image") // Makes sure the serialized name is "image"
    public String getImage() {
        return getImageAsBase64(); // Call the custom method to get Base64
    }
}
