package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    //This controller method is called when the request pattern is of type '/image/{imageId}/{imageTitle}/comments' and also the incoming request is of POST type
    //The method receives all the details of the comments posted for an image to be stored in the database, and now the comment will be sent to the business logic to be persisted in the database
    //After you get the image, set the user of the image by getting the logged in user from the Http Session
    //Set the date on which the comment is posted
    //After storing the comment,Looks for a controller method with request mapping of type '/images/"+ image.getId()+"/"+image.getTitle()'

    //Get the 'comment' request parameter using @RequestParam annotation which is just a string of all the comment
    //Store all the comments in the database and get a list of all the comments using the getAllComments() method
    //set the comments attribute of the image as a list of all the comments returned by the getAllComments() method
    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comments",method = RequestMethod.POST)
    public String createComment(@PathVariable("imageId") Integer imageId, @PathVariable("imageTitle") String title, @RequestParam("comment") String comment, Comment commentObj, HttpSession session) {
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");
        commentObj.setUser(user);
        commentObj.setDate(new Date());
        List<Comment> listComments=commentService.getAllComments();

        image.setComments(listComments);
        commentObj.setImage(image);
        commentObj.setText(comment);
        commentService.createComment(commentObj);
        return "redirect:/images/"+ image.getId()+"/"+image.getTitle();
    }

}
