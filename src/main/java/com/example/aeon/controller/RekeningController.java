package com.example.aeon.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.aeon.model.Rekening;
import com.example.aeon.repository.RekeningRepository;
import com.example.aeon.service.RekeningService;
import com.example.aeon.utils.TemplateResponse;

@RestController
@RequestMapping("/v1/idstar/rekening")
public class RekeningController {

	@Autowired
	RekeningService rekeningService;

	@Autowired
	RekeningRepository rekeningRepository;

	@Autowired
	public TemplateResponse templateResponse;

	@PostMapping("/save/{idkaryawan}")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map> save(@PathVariable(value = "idkaryawan") Long idkaryawan,
			@Valid @RequestBody Rekening objModel) {
		Map obj = rekeningService.insert(objModel, idkaryawan);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Map> update(@RequestBody Rekening objModel) {
		Map obj = rekeningService.update(objModel);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}

	@PutMapping("/update/{idkaryawan}")
	public ResponseEntity<Map> update(@PathVariable(value = "idkaryawan") Long idkaryawan,
			@RequestBody Rekening objModel) {
		Map map = rekeningService.update(objModel, idkaryawan);
		return new ResponseEntity<Map>(map, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {
		Map map = rekeningService.delete(id);
		return new ResponseEntity<Map>(map, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<Map> listByBama(@RequestParam() Integer page, @RequestParam() Integer size,
			@RequestParam(required = false) String nama) {
		Map map = new HashMap();
		Page<Rekening> list = null;
		Pageable show_data = PageRequest.of(page, size, Sort.by("id").descending());// batasin roq

		if (nama != null && !nama.isEmpty()) {
			list = rekeningRepository.findByNamaLike("%" + nama + "%", show_data);
		} else {
			list = rekeningRepository.getAllData(show_data);
		}
		return new ResponseEntity<Map>(templateResponse.templateSukses(list), new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map> getId(@PathVariable(value = "id") Long id) {
		Rekening obj1 = rekeningRepository.getbyID(id);
		return new ResponseEntity<Map>(templateResponse.templateSukses(obj1), HttpStatus.OK);
	}

}
