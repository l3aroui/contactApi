package ma.ram.employeeapp.controllers;
import lombok.RequiredArgsConstructor;
import ma.ram.employeeapp.constants.Constant;
import ma.ram.employeeapp.domain.Contact;
import ma.ram.employeeapp.services.ContactService;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("contact")
@RequiredArgsConstructor
public class ContactController{

    private final ContactService contactService;

    @PostMapping("")
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact){
        return ResponseEntity.created(URI.create("/contact/userID")).body(contactService.addContact(contact));
    }

    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page",defaultValue = "0") int page,
                                                     @RequestParam(value = "size",defaultValue = "10") int size){
        return ResponseEntity.ok().body(contactService.getAllContacts(page,size));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") Long id, @RequestParam("file")MultipartFile file){
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }
    @GetMapping(path = "image/{filename}",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable ("filename") String filename)throws IOException{
        return Files.readAllBytes(Paths.get(Constant.PHOTO_DIRECTORY+filename));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(contactService.getContact(id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Long> deleteContact(@RequestBody Contact contact){
        return ResponseEntity.ok().body(contactService.deleteContact(contact));
    }
}
