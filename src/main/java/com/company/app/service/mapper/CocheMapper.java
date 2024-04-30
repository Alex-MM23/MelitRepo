package com.company.app.service.mapper; 
 
import com.company.app.domain.Coche; 
import com.company.app.service.dto.CocheDTO; 
import java.util.*; 
import org.mapstruct.BeanMapping; 
import org.mapstruct.Mapping; 
import org.mapstruct.Named; 
import org.springframework.stereotype.Service; 
 
@Service 
public class CocheMapper { 
 
    public List<CocheDTO> cochesToCocheDTOs(List<Coche> coche) { 
        return coche.stream().filter(Objects::nonNull).map(this::cocheToCocheDTO).toList(); 
    } 
 
    public CocheDTO cocheToCocheDTO(Coche coche) { 
        return new CocheDTO(coche); 
    } 
 
    public List<Coche> cocheDTOsToCoches(List<CocheDTO> cocheDTOs) { 
        return cocheDTOs.stream().filter(Objects::nonNull).map(this::cocheDTOToCoche).toList(); 
    } 
 
    public Coche cocheDTOToCoche(CocheDTO cocheDTO) { 
        if (cocheDTO == null) { 
            return null; 
        } else { 
            Coche coche = new Coche(); 
            coche.setColor(cocheDTO.getColor()); 
            coche.setNumeroSerie(cocheDTO.getNumeroSerie()); 
            coche.setPrecio(cocheDTO.getPrecio()); 
            coche.setExposicion(cocheDTO.getExposicion()); 
            coche.setMotor(cocheDTO.getTipoCombustible()); 
            coche.setMatricula(cocheDTO.getMatricula()); 
            coche.setModelo(cocheDTO.getModelo()); 
            coche.setFecha_llegada(cocheDTO.getFecha_llegada());
            coche.setFecha_venta(cocheDTO.getFecha_venta());
            return coche; 
        } 
    } 
 
    public Coche cocheFromId(Long id) { 
        if (id == null) { 
            return null; 
        } 
        Coche coche = new Coche(); 
        coche.setId(id); 
        return coche; 
    } 
 
    @Named("id") 
    @BeanMapping(ignoreByDefault = true) 
    @Mapping(target = "id", source = "id") 
    public CocheDTO toDtoId(Coche coche) { 
        if (coche == null) { 
            return null; 
        } 
        CocheDTO cocheDto = new CocheDTO(); 
        cocheDto.setId(coche.getId()); 
        return cocheDto; 
    } 
 
    @Named("idSet") 
    @BeanMapping(ignoreByDefault = true) 
    @Mapping(target = "id", source = "id") 
    public Set<CocheDTO> toDtoIdSet(Set<Coche> coches) { 
        if (coches == null) { 
            return Collections.emptySet(); 
        } 
 
        Set<CocheDTO> cocheSet = new HashSet<>(); 
        for (Coche cocheEntity : coches) { 
            cocheSet.add(this.toDtoId(cocheEntity)); 
        } 
 
        return cocheSet; 
    } 
}