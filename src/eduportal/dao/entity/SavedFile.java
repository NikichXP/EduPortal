package eduportal.dao.entity;

import com.googlecode.objectify.annotation.*;

import lombok.*;

@Entity
@Data
@ToString
@NoArgsConstructor
public class SavedFile {
	
	@Id
	private String id;
	private String filename;
	
}
