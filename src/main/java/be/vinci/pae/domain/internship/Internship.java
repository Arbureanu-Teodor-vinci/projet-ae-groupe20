package be.vinci.pae.domain.internship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of internship for checking methods.
 */
@JsonDeserialize(as = InternshipImpl.class)
public interface Internship extends InternshipDTO {

}
