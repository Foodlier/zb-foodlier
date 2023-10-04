import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/QuotationDetailPage.styled'

const QuotationDetailPage = () => {
  const ESTIMATE_EXAMPLE = {
    recipeId: '1',
    title: '안녕하세요 요리사2입니다',
    content: '치킨마요 덮밥 어떠세요? 달달구리 짭쪼름 하니 맛있읍니다.',
    ingredients: [
      {
        name: '치킨',
        count: '4',
        unit: '개',
      },
      {
        name: '마요네즈',
        count: '2',
        unit: '스푼',
      },
    ],
    difficulty: 'EASY',
    detail: [
      {
        content: '치킨을 남깁니다(매우 어려움)',
      },
      {
        content: '치킨을 데웁니다',
      },
      {
        content: '치킨이랑 마요네즈랑 밥이랑 비빕니다. 간장도 두 스푼 정도',
      },
    ],
    expectedTime: '15분',
  }
  return (
    <>
      <Header />
      <S.RequestContainer>
        <S.RequestHeader>견적서 상세</S.RequestHeader>
        <S.RequestForm action="">
          <S.RequestFormList>
            <S.RequestFormEl>
              <S.ElementTitle>요리사</S.ElementTitle>
              <S.ElementContents>나는 요리사2</S.ElementContents>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>제목</S.ElementTitle>
              <S.ElementContents>{ESTIMATE_EXAMPLE.title}</S.ElementContents>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>내용</S.ElementTitle>
              <S.ElementContents>{ESTIMATE_EXAMPLE.content}</S.ElementContents>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>예상 재료</S.ElementTitle>
              {ESTIMATE_EXAMPLE.ingredients.map(el => (
                <div key={ESTIMATE_EXAMPLE.recipeId}>
                  <S.ElementContents>{`${el.name} ${el.count}${el.unit}`}</S.ElementContents>
                </div>
              ))}
            </S.RequestFormEl>
            <S.RequestFormEl>
              <>
                <S.ElementTitle>예상 난이도</S.ElementTitle>
                {ESTIMATE_EXAMPLE.difficulty === 'EASY' && (
                  <S.ElementContents>쉬움</S.ElementContents>
                )}
                {ESTIMATE_EXAMPLE.difficulty === 'MIDEUM' && (
                  <S.ElementContents>보통</S.ElementContents>
                )}
                {ESTIMATE_EXAMPLE.difficulty === 'HARD' && (
                  <S.ElementContents>어려움</S.ElementContents>
                )}
              </>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>예상 조리순서</S.ElementTitle>
              {ESTIMATE_EXAMPLE.detail.map((el, index) => (
                <div key={ESTIMATE_EXAMPLE.recipeId}>
                  <S.ElementContents>{index + 1}.</S.ElementContents>
                  <S.ElementContents>{el.content}</S.ElementContents>
                </div>
              ))}
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>예상 조리시간</S.ElementTitle>
              <S.ElementContents>
                {ESTIMATE_EXAMPLE.expectedTime}
              </S.ElementContents>
            </S.RequestFormEl>
          </S.RequestFormList>
          <S.ButtonList>
            <S.RejectButton type="button">거절하기</S.RejectButton>
            <S.AcceptButton type="button">수락하기</S.AcceptButton>
          </S.ButtonList>
        </S.RequestForm>
        <S.SpacingDiv />
      </S.RequestContainer>
      <BottomNavigation />
    </>
  )
}

export default QuotationDetailPage
