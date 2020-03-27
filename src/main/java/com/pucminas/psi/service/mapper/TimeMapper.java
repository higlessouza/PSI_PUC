package com.pucminas.psi.service.mapper;


import com.pucminas.psi.domain.*;
import com.pucminas.psi.service.dto.TimeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Time} and its DTO {@link TimeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TimeMapper extends EntityMapper<TimeDTO, Time> {



    default Time fromId(Long id) {
        if (id == null) {
            return null;
        }
        Time time = new Time();
        time.setId(id);
        return time;
    }
}
