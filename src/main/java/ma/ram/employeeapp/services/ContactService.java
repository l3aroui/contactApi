package ma.ram.employeeapp.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ram.employeeapp.constants.Constant;
import ma.ram.employeeapp.domain.Contact;
import ma.ram.employeeapp.repositories.ContactRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepo contactRepo;

    public Page<Contact> getAllContacts(int page,int size){
        return contactRepo.findAll(PageRequest.of(page,size, Sort.by("name")));
    }

    public Contact getContact(Long id){
        return contactRepo.findById(id).orElseThrow(()->new RuntimeException("contact not found"));

    }

    public Contact addContact(Contact contact){
        return contactRepo.save(contact);
    }
    public Long deleteContact(Contact contact){
        contactRepo.delete(contact);
        return contact.getId();
    }

    public String uploadPhoto(Long id, MultipartFile multipartFile){
        log.info("saving photo for user ID: {}",id);
        Contact contact=getContact(id);
        String photoUrl=photoFunction.apply(id,multipartFile);
        contact.setPhotoUrl(photoUrl);
        contactRepo.save(contact);
        return photoUrl;
    }
    private final Function<String,String> fileExtension= filename-> Optional.of(filename).filter(name->name.contains("."))
            .map(name->"."+name.substring(filename.lastIndexOf(".")+1)).orElse(".png");
    private final BiFunction<Long,MultipartFile,String> photoFunction=(id,image)->{
        String fileName=id+fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation= Paths.get(Constant.PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(Files.exists(fileStorageLocation)){Files.createDirectories(fileStorageLocation);}
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(fileName),REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contact/image/"+fileName).toUriString();
        }catch (Exception exception){
            throw new RuntimeException("Unable to save image "+exception);
        }
    };
}