package tech.xavi.wschatspringvue.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xavi.wschatspringvue.dto.JoinRoomDto;
import tech.xavi.wschatspringvue.dto.NewRoomConfigurationDto;
import tech.xavi.wschatspringvue.service.RoomService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/room")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("create")
    public ResponseEntity<?> createRoom(@RequestBody NewRoomConfigurationDto dto){
        return new ResponseEntity<>(roomService.createNewRoom(dto), HttpStatus.CREATED);
    }

    @PostMapping("join")
    public ResponseEntity<?> joinRoom(@RequestBody JoinRoomDto dto){
        return new ResponseEntity<>(roomService.joinRoom(dto), HttpStatus.CREATED);
    }

}
