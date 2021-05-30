package com.website;

import java.util.Random;
import java.util.logging.Logger;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebsiteController {

    private final static Logger log = Logger.getLogger(WebsiteController.class.getSimpleName());

	@GetMapping("/roll")
	public String roll(
		@RequestParam(name="s", required=false, defaultValue="20") String inputSides, 
		@RequestParam(name="n", required=false, defaultValue="1") String inputNum,
		@RequestParam(name="m", required=false, defaultValue="0") String inputMod,
		@RequestParam(name="a", required=false, defaultValue="0") String inputAdvantage,
		@RequestParam(name="ep", required=false, defaultValue="-1") String inputExplodePoint,
		@RequestParam(name="ea", required=false, defaultValue="0") String inputExplodeAll,
		@RequestParam(name="dc", required=false, defaultValue="-1") String inputDC,
		
		Model model) throws Exception {
		int sides, num, mod, explodePoint, advantage, dc;
		boolean explodeAll;
		sides = Integer.parseInt(inputSides);
		num = Integer.parseInt(inputNum);
		mod = Integer.parseInt(inputMod);
		advantage = Integer.parseInt(inputAdvantage);
		explodePoint = Integer.parseInt(inputExplodePoint);
		explodeAll = inputExplodeAll.equals("1");
		dc = Integer.parseInt(inputDC);

		if(advantage != 0 && num == 1){
			num = 2;
		}

		Random rand = new Random();
		int local_num = num;
		List<Integer> rolls = new ArrayList<>();
		if(advantage == 0){
			int total = 0;
			for(int i = 0; i < local_num; i++){
				int roll = Math.abs(rand.nextInt() % sides) + 1;

				rolls.add(roll);

				if(explodePoint != -1 && (roll == explodePoint || (roll > explodePoint && explodeAll))){
					log.info("BOOM! " + roll);
					local_num++;
				}

				total += roll;
			}
		
			total += mod;
			log.info(num + "d" + sides + " + " + mod + " = " + total);

			model.addAttribute("roll", total);
			if(explodePoint != -1){
				model.addAttribute("explode_num", local_num-num);
			}
			if(dc > 0){
				model.addAttribute("success", total >= dc);
			}
		} else {
			int advRoll = advantage > 0 ? 0 : sides*num;
			for(int i = 0; i < local_num; i++){
				int roll = Math.abs(rand.nextInt() % sides) + 1;

				rolls.add(roll + mod);
				if(roll+mod > advRoll && advantage > 0 ){
					advRoll = roll+mod;
				} else if (roll+mod < advRoll && advantage < 0 ){
					advRoll = roll+mod;
				}
			}

			if(dc > 0){
				model.addAttribute("success", advRoll >= dc);
			}
			model.addAttribute("roll", advRoll);
		}

		if(rolls.size() > 1){
			String rolls_list = rolls.stream().map(o -> o.toString()).collect(Collectors.joining(", "));
			log.info(rolls_list);
			model.addAttribute("rolls_list", rolls_list);
		}

		StringBuilder sb = new StringBuilder();

		if(dc > 0){
			sb.append("{");
		}
		if(advantage>0){
			sb.append("[[");
		} else if (advantage<0){
			sb.append("((");
		}
		if(num > 1){
			sb.append(num);
		}
		sb.append("d" + sides);
		if(mod != 0){
			if(mod > 0){
				sb.append(" + " + mod);
			} else {
				sb.append(" - " + Math.abs(mod));
			}
		}
		if(advantage>0){
			sb.append("]]");
		} else if (advantage < 0){
			sb.append("))");
		}
		if(explodePoint != -1 && advantage == 0){
			sb.append("!");
			if(explodeAll){
				sb.append(">");
			}
			if(explodePoint != sides){
				sb.append(explodePoint);
			}
		}
		if(dc > 0){
			sb.append("}>" + dc);
		}

		model.addAttribute("roll_string", sb.toString());

		return "roll";
	}

}
