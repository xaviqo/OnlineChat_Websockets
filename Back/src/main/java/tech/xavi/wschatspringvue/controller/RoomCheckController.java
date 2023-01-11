package tech.xavi.wschatspringvue.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.xavi.wschatspringvue.service.CommonRoomService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/check")
public class RoomCheckController {

    private final CommonRoomService commonRoomService;

    @GetMapping("/room-status/{roomId}")
    public ResponseEntity<?> checkExistsRoom(@PathVariable String roomId){
        return new ResponseEntity<>(commonRoomService.checkRoomStatus(roomId), HttpStatus.OK);
    }

    @GetMapping("/user-inscribed/{roomId}")
    public ResponseEntity<?> checkUserInscribed(@PathVariable String roomId){
        return new ResponseEntity<>(commonRoomService.userIsInscribed(roomId), HttpStatus.OK);
    }

    @GetMapping("/room-admin/{roomId}")
    public ResponseEntity<?> checkWhoIsAdmin(@PathVariable String roomId){
        return new ResponseEntity<>(commonRoomService.whoIsAdmin(roomId), HttpStatus.OK);
    }

}