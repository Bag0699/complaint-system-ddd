package com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.entity;

import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.utils.RelationshipEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aggressors")
public class AggressorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "complaint_id", nullable = false, unique = true)
  private ComplaintEntity complaint;

  @Column(name = "full_name")
  private String fullName;

  @Enumerated(EnumType.STRING)
  private RelationshipEntity relationship;

  @Column(name = "additional_details")
  private String additionalDetails;
}
