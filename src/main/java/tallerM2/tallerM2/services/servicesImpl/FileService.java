package tallerM2.tallerM2.services.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.repository.FileRepository;
import tallerM2.tallerM2.services.IFileService;
import tallerM2.tallerM2.utils.Util;

import java.util.List;
import java.util.Optional;

@Service
public class FileService implements IFileService {

    @Autowired
    FileRepository fileRepository;

    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return File
     */
    @Override
    public File findById(Long id) throws ValueNotFound, BadRequest {
        Optional<File> op = fileRepository.findById(id);
        if(!op.isPresent()){
            new ValueNotFound("File not found");
        }
        return  op.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<File>
     */
    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }
    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     * @return File
     */
    @Override
    public File save(File file){
        return fileRepository.save(Util.convertToDto(file, File.class));
    }
    @Override
    public File update(File from)  {
        return fileRepository.save(Util.convertToDto(from, File.class));
    }



    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param id
     * @return Movile
     *
     */
    @Override
    public void deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<File> op = fileRepository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("File not found");
        }

        fileRepository.eliminarPorId(id);
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     * @return List<file>
     */
    @Override
    public void deleteAll(List<File> files) throws ValueNotFound, BadRequest {
        fileRepository.deleteAll(files);
   }

}
