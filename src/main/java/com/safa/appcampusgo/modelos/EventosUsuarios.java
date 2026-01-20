package com.safa.appcampusgo.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "eventos_usuarios", schema = "campusgo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventosUsuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Cambiado: Usa enum para estado (ASISTENTE, INTERESADO). Almacena como STRING en BD.
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuarios")
    private Usuarios idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_eventos")
    private Evento idEvento;
}