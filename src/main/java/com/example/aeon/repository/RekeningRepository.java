package com.example.aeon.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.aeon.model.Rekening;

@Repository
public interface RekeningRepository extends PagingAndSortingRepository<Rekening,Long> {
	
	@Query("SELECT c from Rekening c")// nama class
    Page<Rekening> getAllData(Pageable pageable);

    @Query("select c from Rekening c WHERE c.id = :id")
    public Rekening getbyID(@Param("id") Long id);
    
    @Query("select c from Rekening c where c.nama= :nama")
    public Page<Rekening> findByNamaLike(String nama , Pageable pageable);
}
