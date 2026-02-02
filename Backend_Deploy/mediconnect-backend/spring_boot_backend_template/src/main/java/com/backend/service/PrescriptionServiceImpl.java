package com.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.backend.dtos.MedicalRecordDto;
import com.backend.entities.PrescriptionItem;
import com.backend.repository.PrescriptionItemRepository;
import com.backend.repository.PrescriptionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
	
	private final PrescriptionRepository prescriptionRepository;
	
	private final PrescriptionItemRepository prescriptionItemRepository;

	@Override
    public long countActivePrescriptions(Long patientId) {

        LocalDate today = LocalDate.now();

        return prescriptionRepository.findByPatientId(patientId)
                .stream()
                .filter(prescription -> {

                    List<PrescriptionItem> items =
                            prescriptionItemRepository
                                    .findByPrescriptionId(prescription.getId());

                    int maxDays = items.stream()
                            .mapToInt(item -> extractDays(item.getDuration()))
                            .max()
                            .orElse(0);

                    LocalDate expiryDate =
                            prescription.getIssuedAt()
                                    .toLocalDate()
                                    .plusDays(maxDays);

                    return !expiryDate.isBefore(today);
                })
                .count();
    }

    private int extractDays(String duration) {
        return Integer.parseInt(duration.split(" ")[0]);
    }

	
	

}
