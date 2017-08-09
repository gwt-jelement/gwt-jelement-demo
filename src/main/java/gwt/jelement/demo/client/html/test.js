var x=function (e, k) {
    return concat(e, typeof k == 'string' && isEnumerable(obj, k) ? [[k, obj[k]]] : []);
}