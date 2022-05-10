package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BoardController {

	// セッションスコープの利用
	@Autowired
	HttpSession session;

	@RequestMapping("/")
	public String index() {
		// セッション情報の破棄
		session.invalidate();
		// board.htmlを表示
		return "board";
	}

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public ModelAndView apply(@RequestParam("name") String name, @RequestParam("feeling") String feeling,
			@RequestParam("contents") String contents, ModelAndView mv) {

		// セッションスコープから書き込みのリスト情報を取得（Object→List<Record>へのキャスト警告を無視）
		@SuppressWarnings("unchecked")
		List<Record> allContents = (List<Record>) session.getAttribute("contentsList");
		// セッションスコープに何も設定されていなければ作成
		if (allContents == null) {
			allContents = new ArrayList<>();
			session.setAttribute("contentsList", allContents);
		}

		// 未入力情報ありの場合
		if (name == null || name.length() == 0 || feeling == null || feeling.length() == 0 || contents == null
				|| contents.length() == 0) {
			// エラーメッセージを追加
			mv.addObject("message", "名前と今の気分と書き込みを入力してください");
		} else {

			// http://またはhttps://始まりの場合はリンクにする
			if (contents.startsWith("http://") || contents.startsWith("https://")) {
				contents = "<a href='" + contents + "'>" + contents + "</a>";
			}
			// 書き込み情報をnewしてリストに追加
			allContents.add(new Record(name, feeling, contents));
		}

		// Modelに情報を追加
		mv.addObject("allContents", allContents);
		// Viewの名前を設定
		mv.setViewName("board");
		return mv;
	}
}
