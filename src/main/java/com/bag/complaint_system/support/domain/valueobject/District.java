package com.bag.complaint_system.support.domain.valueobject;

import lombok.Getter;

@Getter
public enum District {

  // Lima Centro
  LIMA("Lima Cercado", District.ZONE_LIMA_CENTRO),
  BREÑA("Breña", District.ZONE_LIMA_CENTRO),
  LA_VICTORIA("La Victoria", District.ZONE_LIMA_CENTRO),
  RIMAC("Rimac", District.ZONE_LIMA_CENTRO),

  // Lima Norte
  CARABAYLLO("Carabayllo", District.ZONE_LIMA_NORTE),
  COMAS("Comas", District.ZONE_LIMA_NORTE),
  INDEPENDENCIA("Independencia", District.ZONE_LIMA_NORTE),
  LOS_OLIVOS("Los Olivos", District.ZONE_LIMA_NORTE),
  PUENTE_PIEDRA("Puente Piedra", District.ZONE_LIMA_NORTE),
  SAN_MARTIN_DE_PORRES("San Martín de Porres", District.ZONE_LIMA_NORTE),

  // Lima Este
  ATE("Ate", District.ZONE_LIMA_ESTE),
  CIENEGUILLA("Cieneguilla", District.ZONE_LIMA_ESTE),
  EL_AGUSTINO("El Agustino", District.ZONE_LIMA_ESTE),
  SAN_JUAN_DE_LURIGANCHO("San Juan de Lurigancho", District.ZONE_LIMA_ESTE),
  SAN_LUIS("San Luis", District.ZONE_LIMA_ESTE),
  SANTA_ANITA("Santa Anita", District.ZONE_LIMA_ESTE),

  // Lima Sur
  BARRANCO("Barranco", District.ZONE_LIMA_SUR),
  CHORRILLOS("Chorrillos", District.ZONE_LIMA_SUR),
  PACHACAMAC("Pachacamac", District.ZONE_LIMA_SUR),
  PUNTA_HERMOSA("Punta Hermosa", District.ZONE_LIMA_SUR),
  PUNTA_NEGRA("Punta Negra", District.ZONE_LIMA_SUR),
  SAN_JUAN_DE_MIRAFLORES("San Juan de Miraflores", District.ZONE_LIMA_SUR),
  VILLA_EL_SALVADOR("Villa El Salvador", District.ZONE_LIMA_SUR),
  VILLA_MARIA_DEL_TRIUNFO("Villa María del Triunfo", District.ZONE_LIMA_SUR),

  // Lima Moderna
  JESUS_MARIA("Jesús María", District.ZONE_LIMA_MODERNA),
  LINCE("Lince", District.ZONE_LIMA_MODERNA),
  MAGDALENA_DEL_MAR("Magdalena del Mar", District.ZONE_LIMA_MODERNA),
  MIRAFLORES("Miraflores", District.ZONE_LIMA_MODERNA),
  PUEBLO_LIBRE("Pueblo Libre", District.ZONE_LIMA_MODERNA),
  SAN_BORJA("San Borja", District.ZONE_LIMA_MODERNA),
  SAN_ISIDRO("San Isidro", District.ZONE_LIMA_MODERNA),
  SAN_MIGUEL("San Miguel", District.ZONE_LIMA_MODERNA),
  SANTIAGO_DE_SURCO("Santiago de Surco", District.ZONE_LIMA_MODERNA),
  SURQUILLO("Surquillo", District.ZONE_LIMA_MODERNA),

  // Callao
  CALLAO("Callao", District.ZONE_CALLAO);

  private static final String ZONE_LIMA_CENTRO = "Lima Centro";
  private static final String ZONE_LIMA_NORTE = "Lima Norte";
  private static final String ZONE_LIMA_ESTE = "Lima Este";
  private static final String ZONE_LIMA_SUR = "Lima Sur";
  private static final String ZONE_LIMA_MODERNA = "Lima Moderna";
  private static final String ZONE_CALLAO = "Callao";

  private final String displayName;
  private final String zone;

  District(String displayName, String zone) {
    this.displayName = displayName;
    this.zone = zone;
  }
}
