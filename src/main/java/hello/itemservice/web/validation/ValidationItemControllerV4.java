package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }


    //@PostMapping("/add")
    public String addItem1(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // ScriptAssert는 기능이 너무 빈약하다. Object Error 검출 시에는
        // -> 대신 자바 코드로 직접 해주는 것이 더 좋다.
        // 특정 필드가 아닌, 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("globalError", "가격 * 수량의 합은 10000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        // 검증 실패 시 다시 입력 폼으로 이동
        if (bindingResult.hasErrors()) {
            log.info("errors ={} ", bindingResult);
            //model.addAttribute("errors", errors);
            // bindingresult는 자동으로 view에 같이 넘어가기 때문에 model에 추가하는 것 생략 가능
            return "validation/v4/addForm";
        }

        // 성공 시 로직


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItem2(@Validated(value = SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // ScriptAssert는 기능이 너무 빈약하다. Object Error 검출 시에는
        // -> 대신 자바 코드로 직접 해주는 것이 더 좋다.
        // 특정 필드가 아닌, 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("globalError", "가격 * 수량의 합은 10000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        // 검증 실패 시 다시 입력 폼으로 이동
        if (bindingResult.hasErrors()) {
            log.info("errors ={} ", bindingResult);
            //model.addAttribute("errors", errors);
            // bindingresult는 자동으로 view에 같이 넘어가기 때문에 model에 추가하는 것 생략 가능
            return "validation/v4/addForm";
        }

        // 성공 시 로직


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    //@PostMapping("/{itemId}/edit")
    public String edit1(@PathVariable Long itemId, @Validated  @ModelAttribute Item item, BindingResult bindingResult) {
        // 특정 필드가 아닌, 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("globalError", "가격 * 수량의 합은 10000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v4/editForm";
        }


        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String edit2(@PathVariable Long itemId, @Validated(value = UpdateCheck.class)  @ModelAttribute Item item, BindingResult bindingResult) {
        // 특정 필드가 아닌, 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("globalError", "가격 * 수량의 합은 10000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v4/editForm";
        }


        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }
}

