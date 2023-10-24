import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const RequestContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 5%;
  width: 100%;
  margin: 0 auto;
`

export const RequestHeader = styled.h2`
  display: block;
  width: 100%;
  text-align: center;
  padding-bottom: 1%;
  margin-bottom: 3%;
  font-size: ${typography.mobile.mainTitle};
  border-bottom: 1px solid ${palette.divider};

  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
  }
`

export const RequestForm = styled.form`
  padding-top: 4%;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`

export const RequestFormList = styled.ul`
  width: 90%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding-left: 0;
`
export const RequestFormEl = styled.li`
  padding-bottom: 1rem;
  border-bottom: 1px solid ${palette.divider};
  &:last-of-type {
    border-bottom: none;
  }
`

export const ElementTitle = styled.p`
  font-weight: ${weight.subTitle};
  margin-bottom: 1rem;
  color: ${palette.textSecondary};
`
export const ElementContents = styled.span`
  display: inline-block;
  margin-right: 1rem;
  font-size: 2rem;
`

export const IngredientsBoxContainer = styled.div`
  display: flex;
  gap: 1.5rem;
`

export const IngredientsBox = styled.div`
  padding: 1rem 1.4rem;
  border: 0.1rem solid ${palette.divider};
  border-radius: 1rem;
`

export const TaggedRecipe = styled.div`
  width: 20rem;
  height: 2.4rem;
  border-radius: 1rem;
  border: 1px solid ${palette.divider};
`

export const TaggedImg = styled.img`
  width: 100%;
  height: 50%;
  background-color: teal;
  border-radius: 1rem 1rem 0 0;
`

export const TaggedInfo = styled.div`
  height: 50%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 1rem;
`

export const TaggedTopInfo = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`
export const TaggedTitle = styled.span`
  font-weight: bold;
`

export const TaggedLike = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  span {
    font-size: ${typography.mobile.desc};

    ${breakpoints.large} {
      font-size: ${typography.web.desc};
    }
  }
`

export const TaggedBottomInfo = styled.p`
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const ButtonList = styled.div`
  margin: 5rem 0;
  display: flex;
  justify-content: center;
  gap: 1rem;

  ${breakpoints.large} {
    margin: 5rem 0;
  }
`

export const RejectButton = styled.button`
  width: 12rem;
  height: 3.6rem;
  line-height: 3.6rem;
  text-align: center;
  background-color: ${palette.divider};
  color: ${palette.textPrimary};
  font-weight: ${weight.subTitle};
  border-radius: 0.5rem;
`

export const AcceptButton = styled.button`
  width: 12rem;
  height: 3.6rem;
  line-height: 3.6rem;
  text-align: center;
  background-color: ${palette.main};
  color: ${palette.white};
  font-weight: ${weight.subTitle};
  border-radius: 0.5rem;
`
export const SpacingDiv = styled.div`
  width: 100%;
  height: 11rem;
  ${breakpoints.large} {
    height: 2rem;
  }
`
