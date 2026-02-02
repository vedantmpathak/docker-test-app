package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.dtos.PatientDTO;
import com.backend.entities.PatientEntity;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity,Long> {

	//Search Patient's - ALL (UI) --> ADMIN
	@Query("""
	        SELECT u.name
	        FROM PatientEntity p
	        JOIN p.user u
	        WHERE u.isActive = true
	    """)
	List<String> findAllPatientNames();

	
	// Get All Patients --> ADMIN
	@Query("""
	    SELECT new com.backend.dtos.PatientDTO(
	        p.id,
	        u.name,
	        u.email
	    )
	    FROM PatientEntity p
	    JOIN p.user u
	    WHERE u.isActive = true
	""")
	List<PatientDTO> findAllPatients();

}
