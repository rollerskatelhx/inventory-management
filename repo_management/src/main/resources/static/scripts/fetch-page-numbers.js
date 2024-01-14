function fetch_page_numbers(currentPageNum,pageQuantity){
    let begin=1
    let end=1
    if(currentPageNum<4){
        end=Math.min(pageQuantity,7)
    }else{
        begin=Math.min(currentPageNum-3,pageQuantity-6)
        end=Math.min(currentPageNum+3,pageQuantity)
    }
    begin=Math.max(1,begin)
    let pageNumbers=[]
    for(let i=begin;i<=end;i++){
        pageNumbers.push(i)
    }
    return pageNumbers
}