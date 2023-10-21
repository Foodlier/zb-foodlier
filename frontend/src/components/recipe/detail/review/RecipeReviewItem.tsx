import CommonButton from '../../../ui/Button'
import * as S from '../../../../styles/recipe/detail/review/RecipeReviewItem.styled'
import { palette } from '../../../../constants/Styles'
import useIcon from '../../../../hooks/useIcon'
import axiosInstance from '../../../../utils/FetchCall'

interface ReviewItemProps {
  review:{
    recipeId: number
    recipeReviewId: number
    nickname: string
    profileUrl: string
    content: string
    star: number
    cookUrl: string
    createdAt: string
  } 
}

// const deleteReview = async () => {
//   const res = await axiosInstance.delete(`/review/recipe/1`)
//   try {
//     if (res.status === 200) {
//       console.log('리뷰 삭제성공?!')
//     }
//   } catch (error) {
//     console.error('리뷰 삭제 안되서 에러남', error)
//   } finally {
//     console.log('review final')
//   }
// }

function RecipeReviewItem({ review }: ReviewItemProps) {
  const { StarLight, StarFill } = useIcon()

  return (
    <S.ReviewLiBox>
      <S.CookImgWrap>
        <S.CookImg src={review?.cookUrl} alt="요리 이미지" />
      </S.CookImgWrap>
      <S.ReviewCon>
        <S.ReviewTxtWrap>
          <S.ReviewHeader>
            <S.ReviewUserInfo>
              <S.UserImg src={review?.profileUrl} alt={`${review?.nickname}`} />
              <S.UserNickname>{review?.nickname}</S.UserNickname>
            </S.ReviewUserInfo>
            <S.ReviewStar>
              {[1, 2, 3, 4, 5].map(index =>
                index <= review.star ? (
                  <StarFill key={index} size={2} color={palette.yellow} />
                ) : (
                  <StarLight key={index} size={2} color={palette.divider} />
                )
              )}
            </S.ReviewStar>
          </S.ReviewHeader>
          <S.ReviewBody>
            <S.ReviewContent>{review?.content}</S.ReviewContent>
          </S.ReviewBody>
          <S.ReviewFooter>
            <S.ReviewDate>{review?.createdAt}</S.ReviewDate>
          </S.ReviewFooter>
        </S.ReviewTxtWrap>
        <S.ReviewButtonWrap>
          <CommonButton
            size="small"
            color="divider"
            onClick={async () => {
              try {
                // const body = {
                //   content: '하드코딩 테스트 수정 한 내용',
                // }
                const res = await axiosInstance.put(
                  // 해당 리뷰의 id, 바꿀 content, 바꿀 star
                  `review/recipe/${review?.recipeReviewId}?content=${'h23e322i'}!&star=${1}`
                )
                if (res.status === 200) {
                  console.log('리뷰 수정 성공')
                }
              } catch (error) {
                console.error('리뷰 업데이트 실패', error)
              }
            }}
          >
            수정
          </CommonButton>
          <CommonButton
            size="small"
            color="divider"
            onClick={async () => {
              const res = await axiosInstance.delete(
                `/review/recipe/${review?.recipeReviewId}`
              )
              try {
                if (res.status === 200) {
                  console.log('리뷰 삭제성공?!')
                }
              } catch (error) {
                console.error('리뷰 삭제 안되서 에러남', error)
              } finally {
                console.log('review final')
              }
            }}
          >
            삭제
          </CommonButton>
        </S.ReviewButtonWrap>
      </S.ReviewCon>
    </S.ReviewLiBox>
  )
}

export default RecipeReviewItem
