#define bool _Bool

int printf(const char *format, ...);

int scanf(const char *format, ...);

int sprintf(char *str, const char *format, ...);

void *malloc(unsigned int size);

void print(char *s) {
    printf("%s", s);
}

void println(char *s) {
    printf("%s\n", s);
}

void printInt(int n) {
    printf("%d", n);
}

void printlnInt(int n) {
    printf("%d\n", n);
}

char *getString() {
    char *s = (char *) malloc(sizeof(char) * 1024);
    scanf("%s", s);
    return s;
}

int getInt() {
    int n;
    scanf("%d", &n);
    return n;
}

char *toString(int i) {
    char *s;
    s = (char *) malloc(sizeof(char) * 12);
    sprintf(s, "%d", i);
    return s;
}

char *bool_toString(bool b) {
    if (b) {
        return "true";
    } else {
        return "false";
    }
}

void *alloca_helper(int size, int length) {
    int *a = (int *) malloc(size * length + 4);
    a[0] = length;
    return a + 1;
}

int array_size(void *array) {
    return ((int *) array)[-1];
}

int string_length(char *s) {
    int i = 0;
    while (s[i] != '\0') {
        i++;
    }
    return i;
}

char *string_substring(char *s, int left, int right) {
    int len = right - left;
    char *result = (char *) malloc(sizeof(char) * (len + 1));
    for (int i = 0; i < len; i++) {
        result[i] = s[left + i];
    }
    result[len] = '\0';
    return result;
}

int string_parseInt(char *s) {
    int result = 0;
    if (s[0] == '-') {
        for (int i = 1; s[i] != '\0'&&s[i]>='0'&&s[i]<='9'; i++){
            result = result * 10 + s[i] - '0';
        }
        result = -result;
    } else {
        for (int i = 0; s[i] != '\0'&&s[i]>='0'&&s[i]<='9'; i++) {
            result = result * 10 + s[i] - '0';
        }
    }
    return result;
}

int string_compare(char *s1, char *s2) {
    int i = 0;
    while (s1[i] != '\0' && s2[i] != '\0') {
        if (s1[i] != s2[i]) {
            return s1[i] - s2[i];
        }
        i++;
    }
    return s1[i] - s2[i];
}

char *string_concat(char *s1, char *s2) {
    int len1 = string_length(s1);
    int len2 = string_length(s2);
    char *result = (char *) malloc(sizeof(char) * (len1 + len2 + 1));
    for (int i = 0; i < len1; i++) {
        result[i] = s1[i];
    }
    for (int i = 0; i < len2; i++) {
        result[len1 + i] = s2[i];
    }
    result[len1 + len2] = '\0';
    return result;
}

void string_copy(char **s1, char *s2) {
    int len = string_length(s2);
    *s1 = (char *) malloc(sizeof(char) * (len + 1));
    for (int i = 0; i < len; i++) {
        (*s1)[i] = s2[i];
    }
    (*s1)[len] = '\0';
}

int string_ord(char *s, int i) {
    return s[i];
}

void *array_alloca_inside(int size, int dim, int *args, int remaining) {
    if (dim == 1) {
        return alloca_helper(size, *args);
    }
    if (remaining == 1) {
        return alloca_helper(sizeof(void *), *args);
    }
    void *array = alloca_helper(sizeof(void *), *args);
    for (int i = 0; i < *args; i++) {
        ((void **) array)[i] = array_alloca_inside(size, dim - 1, args + 1, remaining - 1);
    }
    return array;
}

void *array_alloca(int size, int dim, int length, ...) {//new int [3][4][5][][]  size=4 dim=5 length=3 ...=3 4 5
    __builtin_va_list ap;
    int *a = (int *) malloc(sizeof(int) * length);
    __builtin_va_start(ap, length);
    for (int i = 0; i < length; i++) {
        a[i] = __builtin_va_arg(ap, int);
    }
    __builtin_va_end(ap);
    return array_alloca_inside(size, dim, a, length);
}