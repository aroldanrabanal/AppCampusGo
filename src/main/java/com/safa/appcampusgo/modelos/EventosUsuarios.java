package com.safa.appcampusgo.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "eventos_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventosUsuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_usuarios")
    private Usuarios idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_eventos")
    private Evento idEvento;
}
