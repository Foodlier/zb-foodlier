import styled from 'styled-components'
import { palette, breakpoints } from '../../../../constants/Styles'

export const ReviewLiBox = styled.li`
  display: flex;
  border: 0.1rem solid ${palette.divider};
  border-radius: 1rem;
  margin-bottom: 1rem;
`
export const CookImgWrap = styled.div`
  margin: 1rem;
  display: flex;
  width: 100%;
  max-width: 10rem;
  height: clamp(10rem, 100%, 10rem);
  justify-content: center;
  align-items: center;
  border-radius: 1rem;
  overflow: hidden;

  ${breakpoints.large} {
    max-width: 15rem;
    height: clamp(15rem, 100%, 15rem);
  }
`
export const CookImg = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`

export const ReviewCon = styled.div`
  position: relative;
  width: 100%;
  padding: 1rem 1rem 5rem 1rem;

  ${breakpoints.large} {
    padding: 0;
  }
`
export const ReviewTxtWrap = styled.div`
  padding: 0rem;

  ${breakpoints.large} {
    padding: 1rem;
  }
`

export const ReviewButtonWrap = styled.div`
  max-width: fit-content;
  width: 100%;
  margin: 1rem;
  position: absolute;
  bottom: 0;
  right: 0;

  ${breakpoints.large} {
    top: 0;
  }
`

export const CommentUserInfo = styled.div`
  display: flex;
  align-items: center;
`
export const ReviewHeader = styled.div`
  display: flex;
  align-items: center;
`

export const ReviewUserInfo = styled.div`
  display: flex;
  align-items: center;
`

export const ReviewStar = styled.div`
  display: flex;
  padding-left: 0.7rem;
`

export const UserImg = styled.img`
  width: 3rem;
  height: 3rem;
  border-radius: 50%;

  ${breakpoints.large} {
    width: 5rem;
    height: 5rem;
  }
`

export const UserNickname = styled.p`
  padding-left: 1rem;
`

export const ReviewBody = styled.div`
  margin: 1rem 0;
`

export const ReviewContent = styled.div``

export const ReviewFooter = styled.div``

export const ReviewDate = styled.p`
  display: flex;
  align-items: center;
  color: ${palette.textSecondary};
`

export const ReviewEdit = styled.form`
  width: 100%;
  padding: 1rem 0;
`
export const ReviewEditInput = styled.textarea`
  width: 100%;
  height: 10rem;
  max-height: auto;
  flex: 1 1 auto;
  padding: 1rem 2rem;
  border-radius: 1rem;
  margin: 0 0 1rem;
  border: 0.1rem solid ${palette.divider};
`

export const ReviewActions = styled.div`
  width: 100%;
`
export const ReviewReplyForm = styled.div`
  width: 100%;
`
export const ReviewReplies = styled.div`
  width: 100%;
`

export const ReviewWriteStar = styled.div`
  display: inline-flex;
`

export const changeReviewStar = styled.div`
  padding-left: 0.3rem;
`
