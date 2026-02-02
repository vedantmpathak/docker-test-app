package com.backend.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.dtos.MedicalRecordDetailsDto;
import com.backend.dtos.MedicalRecordViewDTO;
import com.backend.entities.MedicalRecordEntity;
import com.backend.entities.Prescription;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity,Long> {

	 @Query("""
		        SELECT mr FROM MedicalRecordEntity mr
		        JOIN mr.appointment a
		        JOIN a.doctor d
		        JOIN d.user u
		        WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))
		    """)
	List<MedicalRecordEntity> findRecordsByDoctorName(String name);

	Page<MedicalRecordEntity> findByAppointmentId(Long appointmentId, Pageable pageable);

	//Search Medical Records by patient, doctor or title --> ADMIN
	@Query("""
	        SELECT mr
	        FROM MedicalRecordEntity mr
	        JOIN mr.appointment a
	        JOIN a.patient p
	        JOIN a.doctor d
	        JOIN d.user du
	        JOIN p.user pu
	        WHERE
	        (:patientName IS NULL OR LOWER(pu.name) LIKE LOWER(CONCAT('%', :patientName, '%')))
	        AND
	        (:doctorName IS NULL OR LOWER(du.name) LIKE LOWER(CONCAT('%', :doctorName, '%')))
	        AND
	        (:recordType IS NULL OR LOWER(mr.recordType) LIKE LOWER(CONCAT('%', :recordType, '%')))
	    """)
	List<MedicalRecordEntity> searchRecords(String patientName, String doctorName, String recordType);

	//Search Patient's - ALL TYPES --> ADMIN
	@Query("""
	        SELECT DISTINCT mr.recordType
	        FROM MedicalRecordEntity mr
	        ORDER BY mr.recordType
	    """)
	List<String> findAllRecordTypes();

	
	//Get Particular Medical Records to be shown on UI after clicking --> ADMIN
	@Query("""
		    SELECT new com.backend.dtos.MedicalRecordDetailsDto(
		        mr.id,
		        mr.fileName,
		        mr.recordType,
		        pu.name,
		        du.name,
		        a.appointmentDate,
		        a.startTime,
		        a.endTime,
		        a.reason,
		        a.status
		    )
		    FROM MedicalRecordEntity mr
		    JOIN mr.appointment a
		    JOIN a.patient p
		    JOIN p.user pu
		    JOIN a.doctor d
		    JOIN d.user du
		    WHERE mr.id = :recordId
		""")
		Optional<MedicalRecordDetailsDto> findRecordDetails(
		        @Param("recordId") Long recordId
		);

	
	//Get ALL Medical Records --> ADMIN
	@Query(value = """
		    SELECT
		      mr.medical_record_id,
		      mr.record_type,
		      mr.file_name,

		      a.appointment_id,
		      a.appointment_date,
		      a.start_time,
		      a.end_time,
		      a.reason,
		      a.status,

		      p.patient_id,
		      pu.name AS patient_name,

		      d.doctor_id,
		      du.name AS doctor_name
		    FROM medical_record mr
		    JOIN appointment a ON mr.appointment_id = a.appointment_id
		    JOIN patients p ON a.patient_id = p.patient_id
		    JOIN users pu ON p.user_id = pu.user_id
		    JOIN doctors d ON a.doctor_id = d.doctor_id
		    JOIN users du ON d.user_id = du.user_id
		    ORDER BY a.appointment_date DESC
		    """, nativeQuery = true)
		 List<Object[]> fetchMedicalRecordViewRaw();


}
