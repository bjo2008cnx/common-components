package com.github.common.components.util.constant;

/**
 * 一些常用的常量
 */
public interface CharConstant {
    char AND = '&';
    char AT = '@';
    char ASTERISK = '*';
    char STAR = ASTERISK;
    char BACK_SLASH = '\\';
    char COLON = ':';
    char COMMA = ',';
    char DASH = '-';
    char DOLLAR = '$';
    char DOT = '.';
    char EQUALS = '=';
    char SLASH = '/';
    char HASH = '#';
    char HAT = '^';
    char LEFT_BRACE = '{';
    char LEFT_BRACKET = '(';
    char LEFT_CHEV = '<';
    char NEWLINE = '\n';
    char N = 'n';
    char PERCENT = '%';
    char PIPE = '|';
    char PLUS = '+';
    char QUESTION_MARK = '?';
    char EXCLAMATION_MARK = '!';
    char QUOTE = '\'';
    char RETURN = '\r';
    char TAB = '\t';
    char RIGHT_BRACE = '}';
    char RIGHT_BRACKET = ')';
    char RIGHT_CHEV = '>';
    char SEMICOLON = ';';
    char SPACE = ' ';
    char LEFT_SQ_BRACKET = '[';
    char RIGHT_SQ_BRACKET = ']';
    char UNDERSCORE = '_';
    char Y = 'y';
    char ONE = '1';
    char ZERO = '0';

    String HTML_NBSP = "&nbsp;";
    String HTML_AMP = "&amp";
    String HTML_QUOTE = "&quot;";
    String HTML_LT = "&lt;";
    String HTML_GT = "&gt;";

    String HXWFH = "░▒▣▤▥▦▧▨▩▪▫▬◆◇◈◎●◐◑☉☎☏☜☞☺☻☼♠♡♢♣♤♥♦♧♨♩♪♫♬♭♯";
    String HXWFHS = ".。，、;：？!ˉˇ¨`~ 々～‖∶'`|·… — ～ - 〃‘’“”〝〞〔〕〈〉《》「」『』〖〗【】()[]{｝︻︼﹄﹃";

    //常用的数学符号
    String MATH_SYMBOL = "+-×÷﹢﹣±/=∥∠≌∽≦≧≒﹤﹥≈≡≠=≤≥<>≮≯∷∶∫∮∝∞∧∨∑∏∪∩∈∵∴⊥∥∠⌒⊙√∟⊿㏒㏑%‰";

    //计量符号
    String UNIT_SYMBOL = "㎎㎏㎜㎝㎞㎡㏄㏎㏑㏒㏕℡%‰℃℉°′″$￡￥￠♂♀℅";

    //常用的数学符号
    String NUMBER_SYMBOL = "①②③④⑤⑥⑦⑧⑨⑩㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩№" + "⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇" +
            "ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹ";

    //希腊符号
    String GREEK_SYMBOL = "ΓΔΛΞΠΣΦΨΩαβγδεζνξοπρσηθικλμτυφχψω";

    //俄语
    String RUSSIAN_SYMBOL = "БВГДЁЖИЙКЛПФЦЧШЩЪЫЬЭЮЯ";

    //中文符号
    String ZHCN_SYMBOL = "卍卐卄巜氺兀々〆のぁ〤〥";

    //日语
    String JAPANESE_SYMBOL = "ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴ" +
            "ふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをん ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾ" +
            "タダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵヶ";

    //常用的符号
    String SYMBOL_ALL = "、。·ˉˇ¨〃々—～‖…‘’“”〔〕〈 〉《》「」『』〖〗【】±+-×÷∧∨∑∏∪∩∈√⊥∥∠⌒⊙∫∮≡≌≈∽∝≠≮≯≤≥∞∶ ∵∴∷♂♀°′" +
            "″℃$¤￠￡‰§№☆★〇○●◎◇◆ 回□■△▽⊿▲▼◣◤◢◥▁▂▃▄▅▆▇█▉▊▋▌▍▎▏▓※→←↑↓↖↗↘↙〓 ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹ①②③④⑤⑥⑦⑧⑨⑩" +
            "⒈⒉⒊⒋ ⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩ⅠⅡⅢⅣⅤⅥ" +
            "ⅦⅧⅨⅩⅪⅫ!\"#￥%&'()*+，-./0123456789：;<=>？@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrs" +
            "tuvwxyz{|｝ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴ" +
            "ふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをんァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセ" +
            "ゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵ" +
            "ヶΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθ ικλμνξοπρστυφχψ ω︵︶︹︺︿﹀︽︾﹁﹂﹃﹄︻︼︷︸АБВГДЕЁЖЗИЙКЛМНОПРСТУ" +
            "ФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыь эюāáǎàēéěèī íǐìōóǒòūúǔùǖǘǚǜüêɑ\uE7C7ńň\uE7C8ɡㄅㄆㄇㄈㄉㄊㄋㄌ" +
            "ㄍㄎㄏㄐㄑㄒㄓㄔㄕㄖㄗㄘㄙㄚㄛㄜㄝㄞㄟㄠㄡㄢㄣㄤㄥㄦㄧㄨㄩ︱\uE796︳︴﹏﹋﹌─━│┃┄┅┆ ┇┈┉┊┋┌┍┎┏┐┑┒┓└" +
            "┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄ ╅╆╇╈╉╊╋？㊣㈱曱甴" +
            "囍∟┅﹊﹍╭ ╮╰ ╯ \uE83A_\uE83A ^︵^﹕﹗/\\ \" < > `,·。{}~～() -√ $ @ * & # 卐℡ ぁ〝〞ミ灬№*\uE7E7\uE7F3ㄨ" +
            "≮≯ ﹢﹣/∝≌∽≦≧≒﹤﹥じぷ┗┛￥￡§я-―‥…‰′″℅℉№℡∕∝∣═║╒╓╔╕╖╗╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬╱ ╲╳▔▕" +
            "〆〒〡〢〣〤〥〦〧〨〩㎎ ㎏ ㎜ ㎝ ㎞ ㎡ ㏄ ㏎㏑㏒㏕\uE7C7\uE7C8\uE7E7\uE7E8\uE7E9\uE7EA\uE7EB\uE7EC\uE7ED\uE7EE" +
            "\uE7EF\uE7F0\uE7F1\uE7F2\uE7F3兀︰﹍﹎ ------";
}
