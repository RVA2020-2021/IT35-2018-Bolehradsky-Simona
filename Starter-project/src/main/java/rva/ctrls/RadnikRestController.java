package rva.ctrls;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rva.jpa.Obrazovanje;
import rva.jpa.Preduzece;
import rva.jpa.Radnik;
import rva.jpa.Sektor;
import rva.repository.ObrazovanjeRepository;
import rva.repository.RadnikRepository;
import rva.repository.SektorRepository;

@CrossOrigin
@RestController
@Api(tags = {"Radnik CRUD operacije"})
public class RadnikRestController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RadnikRepository radnikRepository;
	
	@Autowired 
	private SektorRepository sektorRepository;
	
	@Autowired
	private ObrazovanjeRepository obrazovanjeRepository;
	
	@GetMapping("radnik")
	@ApiOperation(value = "Vra�a kolekciju svih radnika iz baze podataka")
	public Collection<Radnik> getRadniks()
	{
		return radnikRepository.findAll();
		
	}
	
	@GetMapping("radnik/{id}")
	@ApiOperation(value = "Vra�a radnika po ID radnika")
	public Radnik getRadnik(@PathVariable("id") Integer id)
	{
		return radnikRepository.getOne(id);
	}
	
	@GetMapping("radnikSektora/{id}")
	@ApiOperation(value = "Vra�a radnike po ID sektora")
	public Collection<Radnik> radnikPoSektoru(@PathVariable("id") Integer id)
	{
		Sektor s = sektorRepository.getOne(id);
		return radnikRepository.findBySektor(s);
	}
	
	
	
	@GetMapping("radnikObrazovanje/{id}")
	@ApiOperation(value = "Vra�a radnike po ID obrazovanja")
	public Collection<Radnik> radnikPoObrazovanju(@PathVariable("id") Integer id)
	{
		Obrazovanje o = obrazovanjeRepository.getOne(id);
		return radnikRepository.findByObrazovanje(o);
	}
	
	
	@GetMapping("radnikIme/{ime}")
	@ApiOperation(value = "Vra�a radnike po imenu radnika")
	public Collection<Radnik> getRadnikByIme(@PathVariable("ime") String ime)
	{
		return radnikRepository.findByImeContainingIgnoreCase(ime);
	}

	
	
	@PostMapping("radnik")
	@ApiOperation(value = "Dodaje novog radnika")
	public ResponseEntity<Radnik> insertRadnik(@RequestBody Radnik  radnik )
	{
		if(!radnikRepository.existsById( radnik.getId()))
		{
			 radnikRepository.save(radnik);
			return new ResponseEntity<Radnik>(HttpStatus.OK);
		}
		return new ResponseEntity<Radnik>(HttpStatus.CONFLICT);
		
	}
	
	@PutMapping("radnik")
	@ApiOperation(value = "A�urira postoje�eg radnika")
	public ResponseEntity<Radnik> updateRadnik(@RequestBody Radnik  radnik )
	{
		if(!radnikRepository.existsById(radnik.getId()))
		{
			return new ResponseEntity<Radnik>(HttpStatus.NO_CONTENT);
		}
		
		radnikRepository.save(radnik);
		return new ResponseEntity<Radnik>(HttpStatus.OK);
	}
	
	
	
	@DeleteMapping("radnik/{id}")
	@ApiOperation(value = "Bri�e postoje�eg radnika")
	public ResponseEntity<Radnik> deleteRadnik(@PathVariable("id") Integer id)
	{
		if(!radnikRepository.existsById(id))
		{
			return new ResponseEntity<Radnik>(HttpStatus.NO_CONTENT);
		}
		
		radnikRepository.deleteById(id);
		if(id==-100 )
		{
			jdbcTemplate.execute(
					"insert into \"radnik\" (\"id\", \"ime\",\"prezime\",  \"broj_lk\", \"obrazovanje\", \"sektor\") "
							+ "values (-100, 'Radnik test', 'test', 12,1,1)"
					
					);
		}
		return new ResponseEntity<Radnik>(HttpStatus.OK);
	}
	
	
}
